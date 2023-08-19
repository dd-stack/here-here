// 라우터 세팅
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

// import layouts
import Header from "./layouts/Header";

// import pages
import Info from "./pages/Info";
import Login from "./pages/Login";
import Redirection from "./pages/Redirection";
import MyPage from "./pages/MyPage/MyPage";
import SentCards from "./pages/MyPage/SentCards";
import ReceivedCards from "./pages/MyPage/ReceivedCards";
import Making from "./pages/Making";
import Card from "./pages/Card";
import ParticipantList from "./pages/ParticipantList";

// import etc
import GlobalStyles from "./styles/GlobalStyles";

export default function App() {
  // 배포 환경에서 console.log 지우기
  if (process.env.NODE_ENV === "production") {
    console.log = function no_console() {};
  }

  return (
    <div className="App">
      <GlobalStyles />
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<Info />} />
          <Route path="/login" element={<Login />} />
          <Route path="/login/oauth2/code/kakao" element={<Redirection />} />
          <Route path="/mypage" element={<MyPage />} />
          <Route path="/sent-cards" element={<SentCards />} />
          <Route path="/received-cards" element={<ReceivedCards />} />
          <Route path="/making" element={<Making />} />
          <Route path="/card/:id" element={<Card />} />
          <Route path="/participant-list" element={<ParticipantList />} />
          <Route path="/*" element={<Navigate to="/" />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
