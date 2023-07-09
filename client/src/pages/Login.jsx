import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { setToken, setUserInfo } from "../store";

import styled from "styled-components";
import { FcConferenceCall } from "react-icons/fc";
import kakaoLogin from "../img/kakao-login.png";

import { login } from "../api/user";

import decodeJwtToken from "../utils/decodeJwtToken";

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
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const isLogin = useSelector((state) => state.user?.userInfo);

  // 이미 로그인이 되어 있다면 홈 화면으로
  useEffect(() => {
    if (isLogin) {
      navigate("/");
    }
  }, [isLogin, navigate]);

  const handleClick = () => {
    login().then((result) => {
      if (result !== "fail") {
        // 응답받은 내용으로 엑세스 토큰 저장
        const token = result.headers.authorization;
        dispatch(setToken(token));
        // 토큰을 디코딩하여 유저 정보 저장
        const payload = decodeJwtToken(token);
        console.log(payload);
        // dispatch(setUserInfo(user));
        // 홈 화면으로
        navigate("/");
      }
      if (result === "fail") {
        // todo: 에러 코드에 따라 분기 처리
        alert("로그인에 실패했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다.");
      }
    });
  };

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
        <img src={kakaoLogin} onClick={handleClick} alt="kakao login button" />
      </LoginContainer>
    </EntireContainer>
  );
}
