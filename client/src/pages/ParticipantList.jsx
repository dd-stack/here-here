import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

import styled from "styled-components";
import { IoIosArrowDropleft } from "react-icons/io";

import { getParticipant } from "../api/card";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  min-height: 100vh;
  padding: 100px 20px;
  background-color: var(--background-color);
`;

const ListTitle = styled.div`
  display: flex;
  align-items: center;
  margin-top: 5%;
  margin-bottom: 20px;
  > span {
    margin: 0 10px;
    font-size: 18px;
  }
  > svg {
    cursor: pointer;
    > path {
      color: var(--gray-color);
    }
  }
`;

const ListContainer = styled.div`
  width: 330px;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
`;

const GuideText = styled.div`
  margin: 40px 20px;
  color: var(--gray-color);
  line-height: 1.5;
`;

const ListItem = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px;
  border-bottom: 1px solid var(--third-theme-color);
  //마지막 줄은 border-bottom 이 없도록
  ${(props) => props.lastItem && `border-bottom: none;`}
  > img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }
`;

export default function ParticipantList() {
  const navigate = useNavigate();

  const cardId = sessionStorage.getItem("cardId");
  const [list, setList] = useState([]);

  useEffect(() => {
    // 컴포넌트가 처음 로드될 때 스크롤을 페이지 제일 위로 이동
    window.scrollTo(0, 0);

    getParticipant(cardId).then((result) => {
      if (result === "fail") {
        alert("리스트를 불러오는데 실패했습니다.");
      } else if (result === "402-fail") {
        alert("접근 권한이 없습니다.");
      } else {
        setList(result.data);
      }
    });
  }, [cardId]);

  return (
    <EntireContainer>
      <ListTitle>
        <IoIosArrowDropleft
          size={24}
          onClick={() => {
            navigate(-1);
          }}
        />
        <span>참석자 명단</span>
      </ListTitle>
      <ListContainer>
        {list?.length === 0 ? (
          <GuideText>
            수락한 사람이 없습니다. <br />
            공유하기를 통해 초대장을 전달해 보세요!
          </GuideText>
        ) : (
          <>
            {list?.map((item, index) => (
              <ListItem key={item.id} lastItem={index === list.length - 1}>
                <img src={item.profileImageURL} alt="kakao profile" />
                <span>{item.nickname}</span>
              </ListItem>
            ))}
          </>
        )}
      </ListContainer>
    </EntireContainer>
  );
}
