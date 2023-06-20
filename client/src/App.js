import { BrowserRouter, Routes, Route } from "react-router-dom";

// import layouts
import Header from "./layouts/Header";

// import pages
import Info from "./pages/Info";
import Login from "./pages/Login";
import Making from "./pages/Making";
import MyPage from "./pages/MyPage";

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
          <Route path="/making" element={<Making />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
