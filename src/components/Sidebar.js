import React, { useState, useEffect } from "react";
import { Link, useLocation } from "react-router-dom";
import "../styles/style.css";
import hamburgerIcon from "../assets/images/icon-hamburger.svg";

// Sidebar 컴포넌트
const Sidebar = () => {
    const location = useLocation();
    const [activeMenu, setActiveMenu] = useState("chat");  // 초기값을 "chat"으로 설정
    const [isSupportDropdownOpen, setIsSupportDropdownOpen] = useState(false);
    const [isCollapsed, setIsCollapsed] = useState(false);

    useEffect(() => {
        // 현재 경로에 따라 활성 메뉴 설정
        const path = location.pathname;
        if (path === "/" || path === "/chat") {  // 루트 경로도 재무분석으로 설정
            setActiveMenu("chat");
        } else if (path === "/suggested-supportPrograms" || path === "/show-supportPrograms") {
            setActiveMenu("support");
        } else if (path === "/dashboard") {
            setActiveMenu("dashboard");
        } else {
            setActiveMenu("chat");  // 기본값을 "chat"으로 설정
        }
        // 페이지 변경 시 드롭다운 상태 초기화
        setIsSupportDropdownOpen(false);
    }, [location]);

    const toggleSupportDropdown = () => {
        setIsSupportDropdownOpen(!isSupportDropdownOpen);
    };

    const toggleSidebar = () => {
        setIsCollapsed(!isCollapsed);
    };

    return (
        <div className={`sidebar-container ${isCollapsed ? 'collapsed' : ''}`}>
            <div className="sidebar">
                <div className="sidebar-profile">
                    <div className="profile-image"></div>
                    <div className="sidebar-helloArea">
                        <span className='sidebar-hello'>안녕하세요, 사용자님!</span>
                    </div>
                </div>
                <nav className="sidebar-content">
                    <Link
                        to="/chat"
                        className={`menu ${activeMenu === 'chat' ? 'active' : ''}`}
                    >
                        <div className="menu-icon">
                            <i className="fi fi-ss-microchip-ai"></i>
                        </div>
                        <span className="menu-text">재무분석</span>
                    </Link>
                    <div
                        className={`menu-dropdown ${isSupportDropdownOpen ? 'open' : ''}`}
                        onMouseEnter={() => setIsSupportDropdownOpen(true)}
                        onMouseLeave={() => setIsSupportDropdownOpen(false)}
                    >
                        <button
                            className={`menu ${activeMenu === 'support' ? 'active' : ''}`}
                            onClick={toggleSupportDropdown}
                        >
                            <div className="menu-icon">
                                <i className="fi fi-ss-form"></i>
                            </div>
                            <span className="menu-text">지원사업</span>
                        </button>
                        <div className="dropdown-content">
                            <Link
                                to="/suggested-supportPrograms"
                                className="dropdown-item"
                            >
                                <span>추천사업</span>
                            </Link>
                            <Link
                                to="/show-supportPrograms"
                                className="dropdown-item"
                            >
                                <span>전체보기</span>
                            </Link>
                        </div>
                    </div>
                    <Link
                        to="/dashboard"
                        className={`menu ${activeMenu === 'dashboard' ? 'active' : ''}`}
                    >
                        <div className="menu-icon">
                            <i className="fi fi-ss-dashboard-monitor"></i>
                        </div>
                        <span className="menu-text">대시보드</span>
                    </Link>
                </nav>
            </div>
            <div className="hamburger-menu" onClick={toggleSidebar}>
                <img src={hamburgerIcon} alt="메뉴" />
            </div>
        </div>
    );
};

export default Sidebar;
