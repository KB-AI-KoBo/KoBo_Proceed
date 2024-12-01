import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Chat from '../pages/Chat';
import ShowPrograms from '../pages/ShowPrograms';
import SuggestedPrograms from '../pages/SuggestedPrograms';
import Dashboard from '../pages/Dashboard';
import '../styles/style.css';
import '../styles/chat.css';

const MainContent = () => {
    return (
        <div className='main-container'>
            <Routes>
                <Route path="/" element={<Chat />} />
                <Route path="/chat" element={<Chat />} />
                <Route path="/show-supportPrograms" element={<ShowPrograms />} />
                <Route path="/suggested-supportPrograms" element={<SuggestedPrograms />} />
                <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </div>
    );
};

export default MainContent;
