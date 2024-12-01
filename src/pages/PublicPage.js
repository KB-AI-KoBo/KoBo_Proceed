import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Header from '../components/Header'; // 헤더 경로
import Sidebar from '../components/Sidebar'; // 사이드바 경로
import MainContent from '../components/MainContent'; // 메인콘텐츠 경로
import Chat from './Chat'; // 메인 콘텐츠에 표시할 Chat 컴포넌트
import SuggestedPrograms from './SuggestedPrograms'; // 다른 페이지들
import ShowPrograms from './ShowPrograms';
import Dashboard from './Dashboard';
import '../styles/style.css'; // 스타일 경로
import '../styles/chat.css';

export const PublicPage = () => {
    return (
        <div className="public-page">
            <div className="overlap-wrapper">
                {/* 헤더 */}
                <Header className="header-instance" />

                {/* 사이드바와 메인 콘텐츠 영역 */}
                <MainContent className="content">
                    {/* 사이드바 */}
                    <Sidebar className="sidebar_instance" />
                    {/* 메인 콘텐츠 영역 */}
                    <div className="main-content">
                        <Routes>
                            <Route path="/" element={<Navigate to="/chat" />} /> {/* 기본 경로를 채팅으로 설정 */}
                            <Route path="/chat" element={<Chat />} /> {/* Chat 컴포넌트 */}
                            <Route path="/suggested-supportPrograms" element={<SuggestedPrograms />} /> {/* 추천 프로그램 */}
                            <Route path="/show-supportPrograms" element={<ShowPrograms />} /> {/* 프로그램 전체 보기 */}
                            <Route path="/dashboard" element={<Dashboard />} /> {/* 대시보드 */}
                        </Routes>
                    </div>
                </MainContent>
            </div>
        </div>
    );
};

export default PublicPage;
