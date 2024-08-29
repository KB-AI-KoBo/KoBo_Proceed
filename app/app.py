from flask import Flask, request, jsonify, render_template, send_from_directory
from dotenv import load_dotenv
from werkzeug.utils import secure_filename
import os
from langchain.chat_models import ChatOpenAI
import requests
from build_vector_db import pdf_to_vector_db, public_to_vector_db
from ExtractLink import ExtractLink
from workflow import run_workflow, extract_final_response
import tempfile
import base64
import time
from flask_cors import CORS
import jwt
from functools import wraps

# 환경 변수 로드
load_dotenv()
openai_api_key = os.getenv("OPENAI_API_KEY")
if not openai_api_key:
    raise ValueError("OpenAI API key not found in environment variables")

# 벡터 데이터베이스 초기화
supporting_db = public_to_vector_db()

# Flask 애플리케이션 설정
app = Flask(__name__, template_folder='./statics/Frontend/public',
            static_folder='./statics')
CORS(app) # CORS 설정
UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'pdf'}
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['SECRET_KEY'] = os.getenv("SECRET_KEY")


# Java 백엔드 URL (실제 URL로 변경 필요)
BACKEND_URL = "http://localhost:5050"

# LLM 초기화
llm = ChatOpenAI(temperature=0.5, model='gpt-4o', openai_api_key=openai_api_key)

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


# 채팅 처리 엔드포인트
@app.route('/chat', methods=['GET', 'POST'])
def chat():
    if request.method == 'POST':
        query = request.form.get('query')
        if not query:
            print("No query provided in AI")
            return jsonify({"error": "No query provided in AI"}), 400

        # 추후 지정
        pdf_path = None
        if 'file' in request.files:
            file = request.files['file']
            if file and allowed_file(file.filename):
                filename = secure_filename(file.filename)
                pdf_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
                file.save(pdf_path)
                print(f"File saved to {pdf_path}")

        # 파일 업로드 경로를 클라이언트로부터 받아서 사용
        uploaded_file_path = request.form.get('filePath')
        if uploaded_file_path:
            pdf_path = uploaded_file_path
            print(f"Using uploaded file path: {pdf_path}")

    # 기본 PDF 경로
    if not pdf_path:
        pdf_path = os.path.join("../test_data/test_input.pdf")
        print(f"Using default PDF path: {pdf_path}")

    try:
        # PDF를 벡터 데이터베이스로 변환
        pdf_db = pdf_to_vector_db(pdf_path)
        print("PDF converted to vector database")

        # 워크플로우 실행
        result = run_workflow(query, pdf_path, openai_api_key, pdf_db, supporting_db, llm)
        print("Workflow executed")

        # 최종 응답 추출
        answer = extract_final_response(result)
        print(f"Extracted answer: {answer}")

        # 데이터 추출
        extracted_data = ExtractLink(answer, llm)
        print(f"Extracted data: {extracted_data}")

        # Java 백엔드로 분석 결과 전송
        try:
            # 문서 업로드
            document_response = requests.post(f"{BACKEND_URL}/api/documents/upload", files={"file": open(pdf_path, "rb")}, timeout=10)
            document_response.raise_for_status()  # HTTP 오류 발생 시 예외 발생
            document_id = document_response.json().get("id")
            print(f"Document uploaded, ID: {document_id}")

            # 분석 결과 저장
            analysis_response = requests.post(
                f"{BACKEND_URL}/analysis/result",
                params={
                    "documentId": document_id,
                    "questionContent": query,
                    "result": answer
                },
                timeout=10
            )
            analysis_response.raise_for_status()
            print("Analysis result saved")

            # 지원 프로그램 요청
            support_program_response = requests.post(
                f"{BACKEND_URL}/support-programs",
                json=extracted_data,  # JSON 데이터로 전송
                timeout=10
            )
            support_program_response.raise_for_status()
            print("Support program request completed")

        except requests.exceptions.RequestException as e:
            print(f"Error sending data to backend: {e}")
            return jsonify({"error": "Backend communication error", "details": str(e)}), 500

        return jsonify({"response": answer, "extracted_data": extracted_data})

    except Exception as e:
        print(f"Error processing request: {e}")
        return jsonify({"error": "Internal server error", "details": str(e)}), 500

    # 기본적으로 'GET' 요청 처리
    return render_template('index.html')

@app.route('/analysis/results/document/<int:document_id>', methods=['GET'])
def get_analysis_results(document_id):
    try:
        response = requests.get(f"{BACKEND_URL}/analysis/results/document/{document_id}")

        if response.status_code != 200:
            print(f"Failed to get analysis results for document ID {document_id}")
            return jsonify({"error": "Failed to get analysis results"}), response.status_code

        print(f"Analysis results retrieved for document ID {document_id}")
        return jsonify(response.json())

    except Exception as e:
        print(f"Error retrieving analysis results: {e}")
        return jsonify({"error": "Internal server error", "details": str(e)}), 500

if __name__ == "__main__":
    os.makedirs(UPLOAD_FOLDER, exist_ok=True)
    app.run(host='0.0.0.0', debug=True, port=5050)
