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
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Header />} />
          <Route path="/my-page" element={<MyPage />} />
          <Route path="/write" element={<Write />} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}
