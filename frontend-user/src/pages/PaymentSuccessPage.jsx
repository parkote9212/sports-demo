import { useNavigate } from 'react-router-dom';

function PaymentSuccessPage() {
  const navigate = useNavigate();
  return (
    <div className="container" style={{ textAlign: 'center', padding: '100px' }}>
      <h1 style={{fontSize:'50px'}}>✅</h1>
      <h2>주문이 정상적으로 접수되었습니다.</h2>
      <p>관리자가 입금을 확인하면 이용권이 발급됩니다.</p>
      <button className="btn-primary" onClick={() => navigate('/mypage/tickets')}>내 보관함으로 이동</button>
    </div>
  );
}
export default PaymentSuccessPage;