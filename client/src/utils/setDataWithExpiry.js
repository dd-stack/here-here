// 데이터를 로컬 스토리지에 저장하고, 유효 시간이 지나면 삭제하는 함수 (시간 단위)
export default function setDataWithExpiry(key, data, expiryTimeInHours) {
  localStorage.setItem(key, JSON.stringify(data));
  setTimeout(() => {
    localStorage.removeItem(key);
    // 페이지 새로고침
    window.location.reload();
  }, expiryTimeInHours * 60 * 60 * 1000);
}
