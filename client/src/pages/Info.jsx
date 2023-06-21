import { useNavigate } from "react-router-dom";

import styled from "styled-components";

const EntireContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const LoginButton = styled.button``;

export default function Info() {
  const navigate = useNavigate();

  const handleLoginBtnClick = () => {
    navigate("/login");
  };

  return (
    <EntireContainer>
      <LoginButton onClick={handleLoginBtnClick}>로그인하러 가기</LoginButton>
    </EntireContainer>
  );
}
