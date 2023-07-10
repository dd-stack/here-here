// 라우터 세팅
import { BrowserRouter, Routes, Route } from "react-router-dom";

// import layouts
import Header from "./layouts/Header";

// import pages
import Info from "./pages/Info";
import Login from "./pages/Login";
import Redirection from "./pages/Redirection";
import MyPage from "./pages/MyPage";
import Making from "./pages/Making";

// import etc
import GlobalStyles from "./styles/GlobalStyles";

export default function App() {
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
          <Route path="/making" element={<Making />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
