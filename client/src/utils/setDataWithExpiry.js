// 데이터를 로컬 스토리지에 저장하고, 유효 시간이 지나면 삭제하는 함수
export default function setDataWithExpiry(key, data, expiryTimeInHours) {
  const now = new Date();
  // 현재 시간에 유효 시간을 더한 시간
  const expiryTime = now.getTime() + expiryTimeInHours * 60 * 60 * 1000;

  const item = {
    data: data,
    expiry: expiryTime,
  };

  localStorage.setItem(key, JSON.stringify(item));

  // 지정된 시간 후에 데이터를 삭제
  setTimeout(() => {
    localStorage.removeItem(key);
    // 페이지 새로고침
    window.location.reload();
  }, expiryTimeInHours * 60 * 60 * 1000);
}
