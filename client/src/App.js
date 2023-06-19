import { BrowserRouter, Routes, Route } from "react-router-dom";

// import layouts
import Header from "./layouts/Header";

// import pages
import Login from "./pages/Login";
import MyPage from "./pages/MyPage";
import Write from "./pages/Write";

export default function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/" element={<Write />} />
          <Route path="/login" element={<Login />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
