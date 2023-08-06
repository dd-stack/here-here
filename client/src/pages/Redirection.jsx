import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setUserInfo } from "../store";

import { getUserInfo } from "../api/user";
import decodeJwtToken from "../utils/decodeJwtToken";

export default function Redirection() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const code = new URL(window.location.href).searchParams.get("code");

  // 인가코드 전달 후 토큰 받기
  useEffect(() => {
    getUserInfo(code).then((result) => {
      if (result !== "fail") {
        // 엑세스 토큰 저장
        const accessToken = result.headers.authorization;
        sessionStorage.setItem("accessToken", accessToken);
        // 엑세스 토큰을 디코딩하여 유저 정보 저장
        const userInfo = decodeJwtToken(accessToken);
        dispatch(setUserInfo(JSON.stringify(userInfo)));
        // 리프레시 토큰 저장
        const refreshToken = result.headers.refreshtoken;
        sessionStorage.setItem("refreshToken", refreshToken);
      }
      if (result === "fail") {
        alert("로그인에 실패했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다.");
      }
      // 이후 페이지 이동
      if (sessionStorage.getItem("cardId")) {
        navigate(`/card/${sessionStorage.getItem("cardId")}`);
      } else {
        navigate("/");
      }
    });
  }, []);

  return <div>로그인 처리 중입니다.</div>;
}
