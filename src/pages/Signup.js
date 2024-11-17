import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/style.css';
import '../styles/signup.css';
import '../styles/styleguide.css';


export const Signup = () => {
    return (
        <div className="signup">
            <div className="signup-container">
                <div className="signup-header">
                    <div className="signup-heading-kobo">Co.worker</div>
                    <div className="signup-heading">회원가입</div>
                </div>

                <div className="signup-content">
                    <div className="signup-section">
                        <div className="signup-background">
                            <div className="signup-title">사용자 정보</div>
                            <div className="signup-input-group">
                                <label className="signup-label">아이디</label>
                                <input type="text" className="signup-input" placeholder="아이디" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">비밀번호</label>
                                <input type="password" className="signup-input" placeholder="비밀번호" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">이름</label>
                                <input type="text" className="signup-input" placeholder="이름" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">이메일 주소</label>
                                <input type="email" className="signup-input" placeholder="이메일 주소" />
                            </div>
                        </div>
                    </div>

                    <div className="signup-section">
                        <div className="signup-background">
                            <div className="signup-title">회사 정보</div>
                            <div className="signup-input-group">
                                <label className="signup-label">회사 이름</label>
                                <input type="text" className="signup-input" placeholder="회사 이름" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">산업 분야</label>
                                <input type="text" className="signup-input" placeholder="산업 분야" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">회사 구분</label>
                                <select className="signup-select">
                                    <option>회사 구분을 선택하세요</option>
                                    <option>대기업</option>
                                    <option>중견기업</option>
                                    <option>중소기업</option>
                                </select>
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">사업자 번호</label>
                                <input type="text" className="signup-input" placeholder="사업자 번호" />
                            </div>
                            <div className="signup-input-group">
                                <label className="signup-label">회사 이메일</label>
                                <input type="email" className="signup-input" placeholder="회사 이메일" />
                            </div>
                        </div>
                    </div>
                </div>

                <button className="signup-button">회원가입</button>
            </div>
        </div>
    );
};

export default Signup;