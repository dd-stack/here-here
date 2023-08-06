import { useNavigate } from "react-router-dom";
import Pagination from "react-js-pagination";
import { format } from "date-fns";

import styled from "styled-components";
import { IoIosArrowDropleft } from "react-icons/io";
import { TiDelete } from "react-icons/ti";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const ListTitle = styled.div`
  display: flex;
  align-items: center;
  margin-top: 10%;
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

const GuideText = styled.span`
  margin: 10% 0;
  color: var(--gray-color);
`;

const ListContainer = styled.div`
  width: 330px;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
`;

const CardItem = styled.div`
  display: flex;
  flex-direction: column;
  padding: 20px;
  border-bottom: 1px solid var(--third-theme-color);
  //마지막 줄은 border-bottom 이 없도록
  ${(props) => props.lastItem && `border-bottom: none;`}
  > span {
    font-size: 14px;
    color: var(--gray-color);
  }
  &:hover {
    background-color: var(--third-theme-color);
  }
`;

const ItemTitle = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  > span {
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
  }
  > svg {
    cursor: pointer;
    > path {
      color: var(--error-color);
    }
  }
`;

const PagingWrapperStyle = styled.div`
  display: flex;
  justify-content: center;
  width: 330px;
  margin-top: 3%;
  .pagination {
    display: flex;
    padding: 0; // 기본 적용 padding 제거
  }
  .pagination li {
    cursor: pointer;
  }
  .pagination li a {
    padding: 10px;
    border-radius: 50px;
    font-size: 14px;
    transition: all 0.3s ease-in-out;
  }
  .pagination li.active a {
    background-color: var(--third-theme-color);
    color: var(--first-theme-color);
  }
  .pagination li.disabled a {
    color: var(--gray-color);
  }
`;

export default function CardList({
  listTitle,
  cards,
  page,
  totalItemsCount,
  onPageChange,
  onDeleteCard,
}) {
  const navigate = useNavigate();

  return (
    <EntireContainer>
      <ListTitle>
        <IoIosArrowDropleft
          size={24}
          onClick={() => {
            navigate(-1);
          }}
        />
        <span>{listTitle}</span>
      </ListTitle>
      {cards?.length === 0 ? (
        <GuideText>{listTitle}이 없습니다.</GuideText>
      ) : (
        <ListContainer>
          {cards?.map((item, index) => (
            <CardItem key={item.id} lastItem={index === cards.length - 1}>
              <ItemTitle>
                <span onClick={() => navigate(`/card/${item.id}`)}>{item.title}</span>
                <TiDelete size={30} onClick={() => onDeleteCard(item.id)} />
              </ItemTitle>
              <span>
                {format(item.startTime, "yy년 MM월 dd일")} ~{" "}
                {format(item.endTime, "yy년 MM월 dd일")}
              </span>
            </CardItem>
          ))}
        </ListContainer>
      )}
      <PagingWrapperStyle>
        <Pagination
          innerClass="pagination"
          // (activePage: 1부터 시작하는 라이브러리용, page: 0부터 시작하는 서버 전송용)
          activePage={page + 1} // 현재 보고있는 페이지
          itemsCountPerPage={5} // 한페이지에 출력할 아이템수
          totalItemsCount={totalItemsCount} // 총 아이템수
          pageRangeDisplayed={5} // 표시할 페이지수
          onChange={onPageChange}
        />
      </PagingWrapperStyle>
    </EntireContainer>
  );
}
