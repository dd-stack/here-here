import { useState } from "react";

import styled from "styled-components";
import { RxHamburgerMenu, RxCross1 } from "react-icons/rx";

const HeaderWrapper = styled.div`
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--third-theme-color);
`;

const LogoStyle = styled.div`
  margin-left: 20px;
`;

const NavListIcon = styled.div`
  margin-right: 20px;
  color: var(--first-theme-color);
  cursor: pointer;
`;

const CustomHamburgerMenu = styled(RxHamburgerMenu)`
  width: 30px;
  height: 30px;
`;

const CustomCross1 = styled(RxCross1)`
  width: 30px;
  height: 30px;
`;

export default function Header() {
  const [NavList, setNavList] = useState(false);

  return (
    <HeaderWrapper>
      <LogoStyle>여기 여기 붙어라</LogoStyle>
      <NavListIcon onClick={() => setNavList(!NavList)}>
        {NavList ? <CustomCross1 /> : <CustomHamburgerMenu />}
      </NavListIcon>
    </HeaderWrapper>
  );
}
