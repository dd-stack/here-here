import { useState } from "react";

import styled from "styled-components";
import { RxHamburgerMenu, RxCross1 } from "react-icons/rx";

import NavList from "../components/NavList";

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

export default function Header() {
  const [isOpen, setIsOpen] = useState(false);

  const handleClick = () => {
    setIsOpen(!isOpen);
  };

  return (
    <>
      <HeaderWrapper>
        <LogoStyle>여기 여기 붙어라</LogoStyle>
        <NavListIcon onClick={handleClick}>
          {isOpen ? <RxCross1 size="30px" /> : <RxHamburgerMenu size="30px" />}
        </NavListIcon>
      </HeaderWrapper>
      <NavList isOpen={isOpen} />
    </>
  );
}
