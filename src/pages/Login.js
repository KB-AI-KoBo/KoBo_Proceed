import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/style.css";
import "../styles/login.css";
import "../styles/styleguide.css";

export const Login = () => {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();

        const userData = {
            email: email,
            password: password,
        };

        try {
            const response = await fetch("http://localhost:5050/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                const data = await response.json();
                if (data.token) {
                    localStorage.setItem("jwtToken", data.token);
                    navigate("/");
                } else {
                    alert("로그인 실패: JWT 토큰이 반환되지 않았습니다.");
                }
            } else {
                alert("로그인 실패: " + response.statusText);
            }
        } catch (error) {
            console.error("Error:", error);
            alert("로그인 중 오류가 발생했습니다.");
        }
    };

    return (
        <div className="login">
            <div className="container">
                <div className="background">
                    <div className="heading-kobo">Co.worker</div>
                    <div className="heading">로그인</div>
                </div>
                <form className="form" onSubmit={handleLogin}>
                    <div className="container-4">
                        <div className="container-wrapper">
                            <input
                                type="email"
                                placeholder="아이디"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                        </div>
                    </div>
                    <div className="container-5">
                        <div className="container-wrapper">
                            <input
                                type="password"
                                placeholder="비밀번호"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                    </div>
                    <div className="container-2">
                        <div className="container-3">
                            <input type="checkbox" className="input" id="rememberMe" />
                            <label htmlFor="rememberMe">아이디 기억하기</label>
                        </div>
                        <a href="/" className="text-wrapper-2">비밀번호 찾기</a>
                    </div>
                    <button type="submit" className="button">로그인</button>
                </form>
                <div className="container-6">
                    <div className="co-worker">Co.worker가 처음이신가요?</div>
                    <a href="/signup" className="link-2">
                        <span className="text-wrapper-5">회원가입</span>
                    </a>
                </div>
            </div>
        </div>
    );
};

export default Login;
