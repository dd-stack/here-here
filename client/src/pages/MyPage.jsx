import { useNavigate } from "react-router-dom";

// redux 관련
import { useSelector } from "react-redux";

export default function MyPage() {
  const navigate = useNavigate();

  const isLogin = useSelector((state) => state.user.userInfo);

  // 로그인이 되어 있지 않다면 로그인 화면으로
  if (!isLogin) {
    navigate("/login");
  }

  return <div>마이 페이지 입니다.</div>;
}
