import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import Swal from "sweetalert2";

import { getReceivedCards } from "../../api/user";
import CardList from "../../components/CardList";

export default function ReceivedCards() {
  const navigate = useNavigate();

  const isLogin = useSelector((state) => state.user?.userInfo);

  // 로그인이 되어 있지 않다면 로그인 화면으로
  useEffect(() => {
    if (!isLogin) {
      navigate("/login");
    }
  }, [isLogin, navigate]);

  const [cards, setCards] = useState([]);
  const [page, setPage] = useState(0);
  const [totalItemsCount, setTotalItemsCount] = useState(0);

  useEffect(() => {
    getReceivedCards(page, 5).then((result) => {
      if (result !== "fail") {
        setCards(result.data.cards);
        setTotalItemsCount(result.data.totalElements);
      }
      if (result === "fail") {
        alert("초대장 리스트를 불러오는데 실패했습니다.");
      }
    });
  }, [page]);

  const onPageChange = (activePage) => {
    // (activePage: 1부터 시작하는 라이브러리용, page: 0부터 시작하는 서버 전송용)
    setPage(activePage - 1);
  };

  const onDeleteCard = (id) => {
    Swal.fire({
      text: "해당 초대장을 삭제하시겠습니까? 받은 초대장은 내가 받은 초대장 목록에서만 삭제됩니다.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "var(--error-color)",
      confirmButtonText: "예",
      cancelButtonText: "아니오",
      padding: "20px 40px 40px",
    }).then(async () => {
      // 삭제 요청
      console.log(id);
    });
  };

  return (
    <CardList
      listTitle={"내가 받은 초대장"}
      cards={cards}
      page={page}
      totalItemsCount={totalItemsCount}
      onPageChange={onPageChange}
      onDeleteCard={onDeleteCard}
    />
  );
}
