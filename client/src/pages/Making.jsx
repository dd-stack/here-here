import { useNavigate } from "react-router-dom";

// redux 관련
import { useSelector } from "react-redux";

export default function Making() {
  const navigate = useNavigate();

  let userInfo = useSelector((state) => state.userInfo);

  // 로그인이 되어 있지 않다면(유저 정보가 빈 객체라면) 로그인 화면으로
  if (!Object.keys(userInfo).length) {
    navigate("/login");
  }

  return <div>작성 페이지 입니다.</div>;
}
