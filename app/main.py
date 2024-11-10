from workflow import run_workflow, extract_final_response
from dotenv import load_dotenv
import os
from build_vector_db import public_to_vector_db, pdf_to_vector_db
from agent_components import initialize_agent_components
from langchain_openai import ChatOpenAI
from ExtractLink import ExtractLink
# recursion_limit
# main에서는 chat history 문제 해결 가능 but app.py에서는 backend와 연결하거나, 새로운 workflow 함수를 작성해야 함
if __name__ == "__main__":
    load_dotenv()
    # API 키 가져오기
    openai_api_key = os.getenv("OPENAI_API_KEY")
    # 에러 처리
    if not openai_api_key:
        raise ValueError("OpenAI API key not found in environment variables")
    supporting_db = public_to_vector_db()
    llm = ChatOpenAI(temperature=0.4, model='gpt-4o',openai_api_key= openai_api_key)
    agent_components = initialize_agent_components(llm)
    chat_history = []
    pdf_path = 'c:/Users/USER/Downloads/[삼성전자]분기보고서(2024.05.16).pdf'
    pdf_db = pdf_to_vector_db(pdf_path)
    while True:        
        query = input("질문을 입력하세요 (종료하려면 'exit' 입력): ")

        # exit 입력시 반복문 탈출
        if query.lower() == 'exit':
            break
        result = run_workflow(query, pdf_path, openai_api_key, pdf_db, supporting_db, llm, agent_components, chat_history)
        # LangGraph의 마지막 답변을 추출
        response = extract_final_response(result)
        print("답변:", response)
        chat_history.append(query)
        chat_history.append(response)
        extracted_data = ExtractLink(response, llm)
        