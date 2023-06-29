import { Link } from "react-router-dom";

// redux 관련
import { useSelector } from "react-redux";

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

const MakingButton = styled(Link)``;

export default function Info() {
  let userInfo = useSelector((state) => state.userInfo);

  return (
    <EntireContainer>
      {Object.keys(userInfo).length ? (
        <MakingButton to="/making">초대장 만들러 가기</MakingButton>
      ) : (
        <LoginButton to="/login">로그인하러 가기</LoginButton>
      )}
    </EntireContainer>
  );
}
