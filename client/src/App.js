import { BrowserRouter, Routes, Route } from "react-router-dom";

// import layouts
import Header from "./layouts/Header";

// import pages
import Info from "./pages/Info";
import Making from "./pages/Making";
import MyPage from "./pages/MyPage";

export default function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<Info />} />
          <Route path="/making" element={<Making />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
