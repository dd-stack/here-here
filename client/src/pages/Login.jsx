import { useNavigate } from "react-router-dom";

// axios 관련
import axios from "../api/core/instance";

// redux 관련
import { useSelector, useDispatch } from "react-redux";
import { setUserInfo, setAccessToken } from "../store";

import styled from "styled-components";

// 아이콘
import { FcConferenceCall } from "react-icons/fc";

// 이미지
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
  const navigate = useNavigate();
  const dispatch = useDispatch();

  let userInfo = useSelector((state) => state.userInfo);

  // 이미 로그인이 되어 있다면 홈 화면으로
  if (userInfo) {
    navigate("/");
  }

  // 카카오 로그인 요청 (임의로 /login으로 get요청)
  const handleClick = async () => {
    try {
      const response = await axios.get("/login");
      // 엑세스 토큰 & 유저 정보 저장
      const token = response.headers.authorization;
      const user = response.data;
      dispatch(setAccessToken(token));
      dispatch(setUserInfo(user));
      // 홈 화면으로
      navigate("/");
    } catch (error) {
      const status = error?.response?.status;
      // todo: 에러 코드에 따라 분기 처리
    }
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
