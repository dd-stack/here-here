import styled from "styled-components";
import { FcConferenceCall } from "react-icons/fc";

import kakaoLogin from "../img/kakao-login.png";

const EntireContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const LoginContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 280px;
  height: 360px;
  border-radius: 20px;
  background-color: #fff;
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
  > img {
    cursor: pointer;
  }
`;

const TextStyle = styled.div`
  margin: 10px 0;
  font-size: 18px;
`;

const LogoStyle = styled.div`
  margin-bottom: 50px;
  font-family: "KyoboHand";
  font-size: 35px;
  font-weight: 600;
  text-align: center;
`;

export default function Login() {
  return (
    <EntireContainer>
      <LoginContainer>
        <FcConferenceCall size="50px" />
        <TextStyle>캐주얼 초대장 서비스</TextStyle>
        <LogoStyle>
          여기 여기
          <br />
          붙어라
        </LogoStyle>
        <img src={kakaoLogin} alt="kakao login button" />
      </LoginContainer>
    </EntireContainer>
  );
}
