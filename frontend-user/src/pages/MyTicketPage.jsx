import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

function MyTicketPage() {
  const navigate = useNavigate();
  const [myTickets, setMyTickets] = useState([]);
  const userName = localStorage.getItem('userName') || '회원';

  useEffect(() => {
    // 백엔드 호출: GET /api/tickets/my
    api.get('/api/tickets/my')
      .then(res => setMyTickets(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="container">
      <h1>{userName}님의 이용권 현황</h1>
      <div className="flex-row">
        {myTickets.length === 0 && <p>보유한 이용권이 없습니다.</p>}

        {myTickets.map((ticket) => {
            // DTO 필드 매핑: remainingCount, totalCount
            const percent = (ticket.remainingCount / ticket.totalCount) * 100;
            return (
                <div key={ticket.ticketId} className="card">
                    <h3>{ticket.ticketName}</h3>
                    <div style={{ fontSize: '2em', fontWeight: 'bold', textAlign: 'center', margin: '15px 0' }}>
                        {ticket.remainingCount} / {ticket.totalCount}회
                    </div>
                    <div style={{ background: '#ccc', height: '10px', width: '100%', marginBottom: '5px' }}>
                        <div style={{ background: '#555', height: '100%', width: `${percent}%` }}></div>
                    </div>
                    <p>만료일: {ticket.expiryDate} <span style={{color:'red'}}>(D-{ticket.dDay})</span></p>
                </div>
            );
        })}

        {/* 구매 페이지 이동 버튼 */}
        <div className="card" 
             style={{ border: '2px dashed #999', cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
             onClick={() => navigate('/mypage/shop')}>
            <div style={{ textAlign: 'center' }}>
                <span style={{ fontSize: '3em' }}>+</span><br />구매하기
            </div>
        </div>
      </div>
    </div>
  );
}
export default MyTicketPage;