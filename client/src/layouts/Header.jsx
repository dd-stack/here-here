import styled from "styled-components";

const HeaderWrapper = styled.div`
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--third-theme-color);
`;

const LogoStyle = styled.div``;

const NavListIcon = styled.div``;

export default function Header() {
  return (
    <HeaderWrapper>
      <LogoStyle>여기 여기 붙어라</LogoStyle>
      <NavListIcon>네브리스트</NavListIcon>
    </HeaderWrapper>
  );
}
