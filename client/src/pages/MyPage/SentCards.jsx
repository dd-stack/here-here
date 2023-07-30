import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";

import styled from "styled-components";

import { getUserInfo } from "../../api/user";

const EntireContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const ListContainer = styled.div`
  width: 330px;
  height: 400px;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
`;

export default function SentCards() {
  const navigate = useNavigate();

  //   const isLogin = useSelector((state) => state.user?.userInfo);

  //   // 로그인이 되어 있지 않다면 로그인 화면으로
  //   useEffect(() => {
  //     if (!isLogin) {
  //       navigate("/login");
  //     }
  //   }, [isLogin, navigate]);

  useEffect(() => {
    getUserInfo().then((result) => {
      if (result !== "fail") {
        //
      }
      if (result === "fail") {
        //
      }
    });
  }, []);

  return (
    <EntireContainer>
      <ListContainer>보낸 초대장</ListContainer>
    </EntireContainer>
  );
}
