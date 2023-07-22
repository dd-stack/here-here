import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { format } from "date-fns";

import styled from "styled-components";

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

export default function Card() {
  const { id } = useParams();

  //   const [card, setCard] = useState({
  //     title: "",
  //     startTime: "",
  //     endTime: "",
  //     background: "#fff",
  //     content: "",
  //     textLocation: "center",
  //     textColor: "#0C0A09",
  //     location: "",
  //   });

  const card = {
    title: "소고기 파티",
    startTime: "2023-07-23T12:00",
    endTime: "2023-07-23T14:00",
    background: "#EFDA92",
    content: "준비물: 입\n\n우리집에서 소고기 꾸어먹자아아",
    textLocation: "center",
    textColor: "#0C0A09",
    location: "서울특별시 강남구 역삼동 825-25",
  };
  const { location } = card;
  const formattedStartTime = format(new Date(card.startTime), "yyyy년 MM월 dd일 a hh:mm");
  const formattedEndTime = format(new Date(card.endTime), "yyyy년 MM월 dd일 a hh:mm");

  //   useEffect(() => {
  //     getCard().then((result) => {
  //       if (result !== "fail") {
  //         setCard(result.data);
  //       }
  //       if (result === "fail") {
  //         alert("초대장 불러오기에 실패했습니다.");
  //       }
  //     });
  //   }, []);

  return (
    <EntireContainer>
      <CardView card={card} />
      <TitleText>- 일시 -</TitleText>
      <ItemText>
        {formattedStartTime}
        <br /> ~ {formattedEndTime}
      </ItemText>
      {location.length === 0 ? null : (
        <>
          <TitleText>- 장소 -</TitleText>
          <KakaoMap location={location} />
        </>
      )}
      <SnsShare cardId={id} />
    </EntireContainer>
  );
}
