import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import styled from "styled-components";

const EntireContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

export default function ReceivedCards() {
  const navigate = useNavigate();

  //   const isLogin = useSelector((state) => state.user?.userInfo);

  //   // 로그인이 되어 있지 않다면 로그인 화면으로
  //   useEffect(() => {
  //     if (!isLogin) {
  //       navigate("/login");
  //     }
  //   }, [isLogin, navigate]);

  return <EntireContainer>받은 초대장</EntireContainer>;
}
