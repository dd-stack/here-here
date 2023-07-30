import { useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { clearUserInfo } from "../../store";

import styled from "styled-components";
import Swal from "sweetalert2";
import { MdArrowForwardIos } from "react-icons/md";

import { deleteUser } from "../../api/user";

const EntireContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  padding: 70px 20px;
  background-color: var(--background-color);
`;

const ProfileContainer = styled.div`
  display: flex;
  align-items: center;
  width: 330px;
  height: 100px;
  margin: 60px 0;
  > img {
    width: 100px;
    height: 100px;
    border-radius: 50%;
  }
`;

const InfoContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-left: 20px;
  gap: 10px;
  .nickname {
    font-size: 20px;
    font-weight: 600;
  }
  .email {
    color: var(--gray-color);
  }
`;

const LinkButton = styled(Link)`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 330px;
  height: 50px;
  padding: 0 20px;
  margin-bottom: 20px;
  border: 2px solid var(--third-theme-color);
  border-radius: 10px;
  &:hover {
    background-color: var(--third-theme-color);
  }
`;

const DeleteUserButton = styled.span`
  position: fixed;
  bottom: 50px;
  left: 50%; // 가운데 정렬을 위해 왼쪽 여백을 절반으로 설정
  transform: translateX(-50%); // 왼쪽 여백을 음수 값으로 이동하여 가운데 정렬
  color: var(--error-color);
  text-decoration: underline;
  cursor: pointer;
`;

export default function MyPage() {
  const navigate = useNavigate();
  const dispatch = useDispatch();

  const userInfo = useSelector((state) => state.user?.userInfo);
  const { nickname, email, profileImageURL } = JSON.parse(userInfo);

  // 로그인이 되어 있지 않다면 로그인 화면으로
  useEffect(() => {
    if (!userInfo) {
      navigate("/login");
    }
  }, [userInfo, navigate]);

  const handleDeleteUser = () => {
    Swal.fire({
      text: "저장되었던 초대장 정보가 모두 사라집니다. 정말로 탈퇴하시겠습니까?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "var(--error-color)",
      confirmButtonText: "예",
      cancelButtonText: "아니오",
      padding: "20px 40px 40px",
    }).then(async (result) => {
      if (result.isConfirmed) {
        await deleteUser().then((result) => {
          if (result === "success") {
            Swal.fire({
              text: "회원 탈퇴가 완료되었습니다.",
              icon: "success",
              confirmButtonColor: "var(--link-color)",
              confirmButtonText: "확인",
              padding: "20px 40px 40px",
            }).then((result) => {
              if (result.isConfirmed) {
                // 로그아웃 처리
                sessionStorage.removeItem("accessToken");
                sessionStorage.removeItem("refreshToken");
                dispatch(clearUserInfo());
                sessionStorage.removeItem("cardId");
                navigate("/");
              }
            });
          }
          if (result === "fail") {
            alert(
              "회원 탈퇴에 실패했습니다. 자세한 내용은 사이트 관리자에게 문의해 주시기 바랍니다."
            );
          }
        });
      }
    });
  };

  return (
    <EntireContainer>
      <ProfileContainer>
        <img src={profileImageURL} alt="kakao profile" />
        <InfoContainer>
          <span className="nickname">{nickname}</span>
          <span className="email">{email}</span>
        </InfoContainer>
      </ProfileContainer>
      <LinkButton to="/sent-cards">
        내가 보낸 초대장
        <MdArrowForwardIos />
      </LinkButton>
      <LinkButton to="/received-cards">
        내가 받은 초대장
        <MdArrowForwardIos />
      </LinkButton>
      <DeleteUserButton onClick={handleDeleteUser}>회원 탈퇴</DeleteUserButton>
    </EntireContainer>
  );
}
