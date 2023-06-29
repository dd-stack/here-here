import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

// redux 관련
import { useSelector } from "react-redux";

import styled from "styled-components";

// 아이콘
import { RxHamburgerMenu, RxCross1 } from "react-icons/rx";

// 컴포넌트
import NavList from "../components/NavList";

const HeaderWrapper = styled.div`
  width: 100%;
  max-width: 640px;
  position: fixed;
  z-index: 1;
`;

const HeaderContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  background-color: var(--third-theme-color);
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

const LogoStyle = styled.div`
  margin-left: 20px;
  font-family: "KyoboHand";
  font-size: 28px;
  font-weight: 600;
  cursor: pointer;
`;

// 로그인이 되었을 때
const NavListIcon = styled.div`
  margin-right: 20px;
  cursor: pointer;
  > svg > path {
    color: var(--first-theme-color);
  }
`;

// 로그인이 되지 않았을 때
const LoginIcon = styled(Link)`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 30px;
  margin-top: 5px;
  margin-right: 20px;
  border: 2px solid #d1c3a8;
  border-radius: 5px;
  background-color: var(--background-color);
`;

export default function Header() {
  const navigate = useNavigate();

  let userInfo = useSelector((state) => state.userInfo);

  const [isOpen, setIsOpen] = useState(false);

  const handleLogoClick = () => {
    navigate("/");
    setIsOpen(false);
  };

  const onMakingNavClick = () => {
    navigate("/making");
    setIsOpen(false);
  };

  const onMypageNavClick = () => {
    navigate("/mypage");
    setIsOpen(false);
  };

  return (
    <HeaderWrapper>
      <HeaderContainer>
        <LogoStyle onClick={handleLogoClick}>여기 여기 붙어라</LogoStyle>
        {Object.keys(userInfo).length ? ( // refactor point: 삼항 연산자 중복 -> 더 보기 좋게 쓸 순 없을까?
          <NavListIcon
            onClick={() => {
              setIsOpen(!isOpen);
            }}
          >
            {isOpen ? <RxCross1 size="30px" /> : <RxHamburgerMenu size="30px" />}
          </NavListIcon>
        ) : (
          <LoginIcon to="/login">로그인</LoginIcon>
        )}
      </HeaderContainer>
      <NavList
        isOpen={isOpen}
        setIsOpen={setIsOpen}
        onMakingNavClick={onMakingNavClick}
        onMypageNavClick={onMypageNavClick}
      />
    </HeaderWrapper>
  );
}
