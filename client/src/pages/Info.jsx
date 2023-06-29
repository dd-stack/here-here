import { Link } from "react-router-dom";

import styled from "styled-components";

const EntireContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const LoginButton = styled(Link)``;

export default function Info() {
  return (
    <EntireContainer>
      <LoginButton to="/login">로그인하러 가기</LoginButton>
    </EntireContainer>
  );
}
