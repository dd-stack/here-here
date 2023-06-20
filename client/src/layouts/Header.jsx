import { useState } from "react";

import styled from "styled-components";
import { RxHamburgerMenu, RxCross1 } from "react-icons/rx";

const HeaderWrapper = styled.div`
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

const NavListContainer = styled.div`
  max-height: ${(props) => (props.isOpen ? "240px" : "0")};
  overflow: hidden;
  transition: max-height 0.4s ease;
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

const NavList = styled.ul`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 240px;
  margin: 0;
  padding: 0 20px;
  background-color: #fff;
  > li {
    margin: 20px 0;
    font-size: 18px;
    cursor: pointer;
  }
`;

export default function Header() {
  const [isOpen, setIsOpen] = useState(false);

  const onClick = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <HeaderWrapper>
        <LogoStyle>여기 여기 붙어라</LogoStyle>
        <NavListIcon onClick={onClick}>
          {isOpen ? <RxCross1 size="30px" /> : <RxHamburgerMenu size="30px" />}
        </NavListIcon>
      </HeaderWrapper>
      <NavListContainer isOpen={isOpen}>
        <NavList>
          <li>초대장 만들기</li>
          <li>마이페이지</li>
          <li>로그아웃</li>
        </NavList>
      </NavListContainer>
    </>
  );
}
