import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { setToken, setUserInfo } from "../store";

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
        const token = result.headers.authorization;
        dispatch(setToken(token));
        // 토큰을 디코딩하여 유저 정보 저장
        const user = decodeJwtToken(token);
        dispatch(setUserInfo(user));
      }
      if (result === "fail") {
        // todo: 에러 코드에 따라 분기 처리
        alert("로그인에 실패했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다.");
      }
      // 이전 화면으로 돌아가게 해야 하지 않을까?
      navigate("/");
    });
  }, []);

  return <div>로그인 처리 중입니다.</div>;
}
