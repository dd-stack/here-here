import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import Swal from "sweetalert2";

import { getSentCards } from "../../api/user";
import { deleteCard } from "../../api/card";
import CardList from "../../components/CardList";

export default function SentCards() {
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
    getSentCards(page, 5).then((result) => {
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
      text: "해당 초대장을 삭제하시겠습니까? 삭제된 초대장은 다른 유저의 초대장 목록에서도 삭제됩니다.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "var(--error-color)",
      confirmButtonText: "예",
      cancelButtonText: "아니오",
      padding: "20px 40px 40px",
    }).then((result) => {
      if (result.isConfirmed) {
        // 삭제 요청
        deleteCard(id).then((result) => {
          if (result === "success") {
            Swal.fire({
              text: "초대장이 삭제되었습니다.",
              icon: "success",
              confirmButtonColor: "var(--link-color)",
              confirmButtonText: "확인",
              padding: "20px 40px 40px",
            }).then(() => {
              // 삭제 후 새로고침
              window.location.reload();
            });
          }
          if (result === "fail") {
            alert("초대장 삭제에 실패했습니다. 다시 시도해 주시기 바랍니다.");
          }
        });
      }
    });
  };

  return (
    <CardList
      listTitle={"내가 보낸 초대장"}
      cards={cards}
      page={page}
      totalItemsCount={totalItemsCount}
      onPageChange={onPageChange}
      onDeleteCard={onDeleteCard}
    />
  );
}
