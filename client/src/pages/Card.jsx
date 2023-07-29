import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import { format } from "date-fns";

import styled from "styled-components";
import Swal from "sweetalert2";

import { getCard } from "../api/card";
import CardView from "../components/CardView";
import KakaoMap from "../components/KakaoMap";
import SnsShare from "../components/SnsShare";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 150px 20px;
  background-color: var(--background-color);
`;

const TitleText = styled.span`
  margin: 70px 0 20px 0;
  font-size: 20px;
`;

const ItemText = styled.span`
  text-align: center;
  line-height: 1.5;
`;

const JoinButton = styled.button`
  width: 150px;
  height: 50px;
  margin-top: 150px;
  border: 2px solid var(--second-theme-color);
  border-radius: 10px;
  font-size: 15px;
  font-weight: 600;
  background-color: var(--third-theme-color);
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

export default function Card() {
  const navigate = useNavigate();
  const { id } = useParams();
  sessionStorage.setItem("cardId", id);

  const isLogin = useSelector((state) => state.user?.userInfo);

  const [card, setCard] = useState({
    title: "",
    startTime: "",
    endTime: "",
    background: "#fff",
    content: "",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "",
  });

  const { location } = card;
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");

  useEffect(() => {
    // 컴포넌트가 처음 로드될 때 스크롤을 페이지 제일 위로 이동
    window.scrollTo(0, 0);
  }, []);

  useEffect(() => {
    getCard(id).then((result) => {
      if (result !== "fail") {
        console.log(result.data);
        setCard(result.data);
        // 날짜 포맷 변경
        const formattedStartTime = format(
          new Date(result.data.startTime),
          "yyyy년 MM월 dd일 a hh:mm"
        );
        const formattedEndTime = format(new Date(result.data.endTime), "yyyy년 MM월 dd일 a hh:mm");
        setStartTime(formattedStartTime);
        setEndTime(formattedEndTime);
      }
      if (result === "fail") {
        alert("초대장 불러오기에 실패했습니다.");
      }
    });
  }, [id]);

  const handleClick = () => {
    if (!isLogin) {
      Swal.fire({
        title: "로그인이 필요한 서비스입니다.",
        text: "카카오로 간편하게 로그인한 후, 톡캘린더를 연동하고 받은 초대장을 관리해 보세요!",
        icon: "info",
        showCancelButton: true,
        confirmButtonColor: "var(--link-color)",
        confirmButtonText: "확인",
        cancelButtonText: "취소",
        padding: "20px 40px 40px",
      }).then((result) => {
        if (result.isConfirmed) {
          navigate("/login");
        } else {
          navigate(`/card/${id}`);
        }
      });
    }
    if (isLogin) {
      // 1. 톡캘린더 일정 등록 요청
      // 2. 내가 받은 초대장 목록 업데이트 요청
    }
  };

  return (
    <EntireContainer>
      <CardView card={card} />
      <TitleText>- 일시 -</TitleText>
      <ItemText>
        {startTime}
        <br /> ~ {endTime}
      </ItemText>
      {location.length === 0 ? null : (
        <>
          <TitleText>- 장소 -</TitleText>
          <KakaoMap location={location} />
        </>
      )}
      <JoinButton type="button" onClick={handleClick}>
        수락하기
      </JoinButton>
      <SnsShare cardId={id} />
    </EntireContainer>
  );
}
