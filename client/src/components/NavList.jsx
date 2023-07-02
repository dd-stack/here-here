import { useNavigate } from "react-router-dom";

// redux 관련
import { useDispatch } from "react-redux";
import { clearToken, clearUserInfo } from "../store";

import styled from "styled-components";

// 아이콘
import { FcCloseUpMode, FcBusinesswoman, FcMinus } from "react-icons/fc";

const NavListContainer = styled.div`
  max-height: ${(props) => (props.open ? "250px" : "0")};
  overflow: hidden;
  transition: max-height 0.4s ease;
  box-shadow: 0 1px 2px hsla(0, 0%, 0%, 0.05), 0 1px 4px hsla(0, 0%, 0%, 0.05),
    0 2px 8px hsla(0, 0%, 0%, 0.05);
`;

const ListItems = styled.ul`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 250px;
  margin: 0;
  padding: 0 20px;
  background-color: #fff;
  > li {
    display: flex;
    gap: 5px;
    margin: 25px 0;
    font-size: 18px;
    cursor: pointer;
  }
`;

export default function NavList({ isOpen, setIsOpen, onMakingNavClick, onMypageNavClick }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  // 로그아웃
  const handleClick = () => {
    // 엑세스 토큰 & 유저 정보 삭제
    dispatch(clearToken());
    dispatch(clearUserInfo());
    // 홈 화면으로 라우팅, NavList 닫기
    navigate("/");
    setIsOpen(false);
  };

  return (
    <NavListContainer open={isOpen}>
      <ListItems>
        <li onClick={onMakingNavClick}>
          <FcCloseUpMode />
          초대장 만들기
        </li>
        <li onClick={onMypageNavClick}>
          <FcBusinesswoman />
          마이페이지
        </li>
        <li onClick={handleClick}>
          <FcMinus />
          로그아웃
        </li>
      </ListItems>
    </NavListContainer>
  );
}
