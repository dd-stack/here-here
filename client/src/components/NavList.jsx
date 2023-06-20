import styled from "styled-components";

const NavListContainer = styled.div`
  max-height: ${(props) => (props.open ? "240px" : "0")};
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
  height: 240px;
  margin: 0;
  padding: 0 20px;
  background-color: #fff;
  > li {
    margin: 20px 0;
    font-size: 18px;
    cursor: pointer;
  }
`;

export default function Modal({ isOpen }) {
  return (
    <NavListContainer open={isOpen}>
      <ListItems>
        <li>초대장 만들기</li>
        <li>마이페이지</li>
        <li>로그아웃</li>
      </ListItems>
    </NavListContainer>
  );
}
