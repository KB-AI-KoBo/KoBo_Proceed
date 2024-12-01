import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';  // useNavigate 추가
import kollyImage from '../assets/images/kolly.png';
import '../styles/bootstrap.min.css';
import '../styles/style.css';

const Sidebar = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [isSupportDropdownOpen, setSupportDropdownOpen] = useState(false);
    const [isSettingsDropdownOpen, setSettingsDropdownOpen] = useState(false);

    const toggleSupportDropdown = () => {
        setSupportDropdownOpen(!isSupportDropdownOpen);
    };

    const toggleSettingsDropdown = () => {
        setSettingsDropdownOpen(!isSettingsDropdownOpen);
    };

    useEffect(() => {
        if (location.pathname === '/login' || location.pathname === '/signup') {
            setSupportDropdownOpen(false);
            setSettingsDropdownOpen(false);
        }
    }, [location]);

    const handleLoginClick = () => {
        navigate('/login');  // 로그인 클릭 시 /login 경로로 이동
    };

    return (
        <div className="sidebar pe-4 pb-3">
            <nav className="navbar bg-light navbar-light">
                <Link to="/" className="navbar-brand mx-4 mb-3">
                    <h3 className="text-primary">KoBo</h3>
                </Link>
                <div className="d-flex align-items-center ms-4 mb-4">
                    <div className="position-relative">
                        <img className="rounded-circle" src={kollyImage} alt="user-profile" style={{ width: '50px', height: '50px' }} />
                    </div>
                    <div className="ms-3">
                        <h6 className="mb-0">콜리</h6>
                        <span>스타프렌즈</span>
                    </div>
                </div>
                <div className="navbar-nav w-100">
                    <Link
                        to="/chat"
                        className={`nav-item nav-link ${location.pathname === '/chat' ? 'active' : ''}`}
                    >
                        <i className="fi fi-ss-microchip-ai me-2"></i>재무분석
                    </Link>
                    <div className={`nav-item dropdown ${isSupportDropdownOpen ? 'show' : ''}`}>
                        <a
                            href="#!"
                            className="nav-link dropdown-toggle"
                            onClick={toggleSupportDropdown}
                            aria-expanded={isSupportDropdownOpen}
                        >
                            <i className="fi fi-ss-form me-2"></i>지원사업
                        </a>
                        <div className={`dropdown-menu bg-transparent border-0 ${isSupportDropdownOpen ? 'show' : ''}`}>
                            <Link
                                to="/suggested-supportPrograms"
                                className={`dropdown-item ${location.pathname === '/suggested-supportPrograms' ? 'active' : ''}`}
                            >
                                추천사업
                            </Link>
                            <Link
                                to="/show-supportPrograms"
                                className={`dropdown-item ${location.pathname === '/show-supportPrograms' ? 'active' : ''}`}
                            >
                                전체보기
                            </Link>
                        </div>
                    </div>
                    <Link
                        to="/dashboard"
                        className={`nav-item nav-link ${location.pathname === '/dashboard' ? 'active' : ''}`}
                    >
                        <i className="fi fi-ss-dashboard-monitor me-2"></i>대시보드
                    </Link>
                    <div className={`nav-item dropdown ${isSettingsDropdownOpen ? 'show' : ''}`}>
                        <a
                            href="#!"
                            className="nav-link dropdown-toggle"
                            onClick={toggleSettingsDropdown}
                            aria-expanded={isSettingsDropdownOpen}
                        >
                            <i className="fi fi-ss-settings me-2"></i>설정
                        </a>
                        <div className={`dropdown-menu bg-transparent border-0 ${isSettingsDropdownOpen ? 'show' : ''}`}>
                            <a
                                href="#!"
                                className={`dropdown-item ${location.pathname === '/login' ? 'active' : ''}`}
                                onClick={handleLoginClick}  // 로그인 클릭 시 함수 호출
                            >
                                로그인
                            </a>
                            <Link
                                to="/signup"
                                className={`dropdown-item ${location.pathname === '/signup' ? 'active' : ''}`}
                            >
                                회원가입
                            </Link>
                        </div>
                    </div>
                </div>
            </nav>
        </div>
    );
};

export default Sidebar;

// 사이드바 스타일
const sidebarStyles = `
.sidebar {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    width: 250px;
    height: 100vh;
    overflow-y: auto;
    background: var(--light);
    transition: 0.5s;
    z-index: 999;
}

.sidebar .navbar {
    padding: 15px 0;
}

.sidebar .navbar-nav .nav-link {
    padding: 7px 20px;
    color: var(--dark);
    font-weight: 500;
    border-left: 3px solid var(--light);
    border-radius: 0 30px 30px 0;
    outline: none;
}

.sidebar .navbar-nav .nav-link:hover,
.sidebar .navbar-nav .nav-link.active {
    color: var(--primary);
    background: #FFFFFF;
    border-color: var(--primary);
}

.sidebar .navbar-nav .nav-link i {
    width: 40px;
    height: 40px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    background: #FFFFFF;
    border-radius: 40px;
}

.sidebar .navbar-nav .nav-link:hover i,
.sidebar .navbar-nav .nav-link.active i {
    background: var(--light);
}

.sidebar .navbar-nav .dropdown-toggle::after {
    position: absolute;
    top: 15px;
    right: 15px;
    border: none;
    content: "\f107";
    font-family: "Font Awesome 5 Free";
    font-weight: 900;
    transition: .5s;
}

.sidebar .navbar-nav .dropdown-toggle[aria-expanded=true]::after {
    transform: rotate(-180deg);
}

.sidebar .navbar-nav .dropdown-item {
    padding-left: 25px;
    border-radius: 0 30px 30px 0;
}
`;
