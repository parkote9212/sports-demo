import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/axiosConfig';

function TicketShopPage() {
  const navigate = useNavigate();
  
  // 1. 상태 관리 (State)
  const [products, setProducts] = useState([]);           // 상품 목록 (DB에서 가져옴)
  const [selectedProduct, setSelectedProduct] = useState(null); // 선택된 상품
  const [isModalOpen, setIsModalOpen] = useState(false);  // 모달 표시 여부
  const [agreed, setAgreed] = useState(false);            // 약관 동의 여부
  const [paymentMethod, setPaymentMethod] = useState("BANK_TRANSFER"); // 결제 수단
  const [userPoints, setUserPoints] = useState(0);        // 사용자 보유 포인트
  
  // 로그인한 사용자 이름 (없으면 기본값)
  const userName = localStorage.getItem('userName') || '회원';

  // 2. 화면 로드 시 데이터 조회 (useEffect)
  useEffect(() => {
    const fetchData = async () => {
        try {
            // (1) 상품 목록 조회 (판매 중인 것만)
            // Backend: TicketProductController.getTicketProducts()
            const productRes = await api.get('/api/ticket-products'); 
            console.log("상품 목록 로드:", productRes.data);
            setProducts(productRes.data);

            // (2) 내 포인트 조회
            // Backend: MemberController.getCashPoint()
            const userId = localStorage.getItem('userId');
            if (userId) {
                const pointRes = await api.get(`/api/members/${userId}/cash-point`);
                console.log("포인트 조회:", pointRes.data);
                setUserPoints(pointRes.data);
            }
        } catch (error) {
            console.error("데이터 로딩 중 에러 발생:", error);
        }
    };
    fetchData();
  }, []);

  // 3. 모달 열기 함수
  const openModal = () => {
    if (!selectedProduct) return alert("상품을 먼저 선택해주세요.");
    setAgreed(false);               // 약관 동의 초기화
    setPaymentMethod("BANK_TRANSFER"); // 결제수단 초기화
    setIsModalOpen(true);           // 모달 Open
  };

  // 4. 결제 요청 함수 (API 호출)
  const handlePayment = async () => {
    // 유효성 검사 1: 약관 동의
    if (!agreed) return alert("구매 조건 및 환불 규정에 동의해야 합니다.");

    // 유효성 검사 2: 포인트 잔액 확인 (클라이언트 레벨 방어)
    if (paymentMethod === "POINT" && userPoints < selectedProduct.price) {
        return alert("포인트가 부족하여 결제할 수 없습니다.");
    }

    const userId = localStorage.getItem('userId');
    
    // DTO 생성 (PaymentRequest 구조)
    const requestDto = {
        memberId: Number(userId),
        productId: selectedProduct.id, // Entity의 ID
        method: paymentMethod // "POINT" or "BANK_TRANSFER"
    };

    try {
        // Backend: PaymentController.purchaseTicket()
        const response = await api.post('/api/payments/purchase', requestDto);
        console.log("결제 성공:", response.data);
        
        setIsModalOpen(false);
        navigate('/payment/success'); // 성공 페이지로 이동
    } catch (error) {
        console.error("결제 실패:", error);
        alert(error.response?.data?.message || "결제 요청 중 오류가 발생했습니다.");
    }
  };

  // 현재 시간 포맷팅 헬퍼 함수
  const getCurrentTime = () => {
    const now = new Date();
    return now.toLocaleString('ko-KR', { 
        year: 'numeric', month: '2-digit', day: '2-digit', 
        hour: '2-digit', minute: '2-digit' 
    });
  };

  // 5. 화면 렌더링
  return (
    <div className="container">
      <h2>이용권 구매 (Shop)</h2>
      
      {/* 상단: 포인트 정보 */}
      <div style={{textAlign:'right', marginBottom:'10px'}}>
        <span style={{background:'#eee', padding:'5px 15px', borderRadius:'15px', fontSize:'0.9em'}}>
            💰 내 보유 포인트: <strong>{userPoints.toLocaleString()} P</strong>
        </span>
      </div>

      <div className="flex-row">
        {/* 왼쪽: 상품 목록 리스트 */}
        <div className="box" style={{ width: '60%' }}>
            <h3>상품 목록</h3>
            {products.length === 0 && <p style={{padding:'20px'}}>판매 중인 상품이 없습니다.</p>}
            
            {products.map(prod => (
                <div key={prod.id} className="card" 
                     onClick={() => setSelectedProduct(prod)}
                     style={{ 
                        marginBottom:'10px', cursor:'pointer', 
                        border: selectedProduct?.id === prod.id ? '3px solid #007bff' : '1px solid #ddd' 
                     }}>
                    {/* [백엔드 개발자 Check Point]
                       TicketProduct Entity의 필드명과 일치해야 합니다.
                       만약 DB 컬럼이 ticketName이면 {prod.ticketName}으로 수정하세요.
                    */}
                    <h4>{prod.name}</h4> 
                    <p>{(prod.price || 0).toLocaleString()}원</p>
                    
                    {selectedProduct?.id === prod.id && <span style={{color:'#007bff', fontWeight:'bold'}}>✔ 선택됨</span>}
                </div>
            ))}
        </div>

        {/* 오른쪽: 선택 상품 요약 사이드바 */}
        <div className="box" style={{ width: '35%' }}>
            <h3>선택 상품 요약</h3>
            <p style={{fontSize:'1.2em'}}><strong>{selectedProduct ? selectedProduct.name : '-'}</strong></p>
            <hr/>
            <p>결제 금액: <span style={{color:'#e74c3c', fontSize:'1.5em', fontWeight:'bold'}}>
                {selectedProduct ? (selectedProduct.price || 0).toLocaleString() : '0'}원
            </span></p>
            <button className="btn-primary" style={{width:'100%', marginTop:'20px', height:'50px'}} onClick={openModal}>
                결제 진행하기
            </button>
        </div>
      </div>

      {/* 결제 상세 모달 (팝업) */}
      {isModalOpen && selectedProduct && (
        <div className="modal-overlay">
            <div className="modal-checkout">
                <h2 style={{textAlign:'center', borderBottom:'2px solid #333', paddingBottom:'10px'}}>주문 / 결제 확인</h2>
                
                <div style={{display:'flex', gap:'30px', marginTop:'20px'}}>
                    
                    {/* 모달 왼쪽: 주문 정보 */}
                    <div style={{flex:1}}>
                        <h4 style={{marginBottom:'10px'}}>1. 주문 정보</h4>
                        <p><strong>상품명:</strong> {selectedProduct.name}</p>
                        <p><strong>주문자:</strong> {userName}</p>
                        <p><strong>결제일시:</strong> {getCurrentTime()}</p>
                    </div>

                    {/* 모달 오른쪽: 결제 수단 및 동의 */}
                    <div style={{flex:1, borderLeft:'1px solid #ddd', paddingLeft:'30px'}}>
                        <h4 style={{marginBottom:'10px'}}>2. 결제 수단 선택</h4>
                        
                        <div style={{display:'flex', gap:'10px', marginBottom:'15px'}}>
                            <label style={{cursor:'pointer'}}>
                                <input type="radio" name="payMethod" 
                                       checked={paymentMethod === 'BANK_TRANSFER'} 
                                       onChange={() => setPaymentMethod('BANK_TRANSFER')} /> 무통장 입금
                            </label>
                            <label style={{cursor:'pointer'}}>
                                <input type="radio" name="payMethod" 
                                       checked={paymentMethod === 'POINT'} 
                                       onChange={() => setPaymentMethod('POINT')} /> 포인트 사용
                            </label>
                        </div>

                        {/* 포인트 결제 선택 시 계산기 UI */}
                        {paymentMethod === 'POINT' && (
                            <div style={{background:'#eef', padding:'10px', borderRadius:'5px', fontSize:'0.9em', marginBottom:'10px'}}>
                                <div style={{display:'flex', justifyContent:'space-between'}}>
                                    <span>보유 포인트:</span>
                                    <span>{userPoints.toLocaleString()} P</span>
                                </div>
                                <div style={{display:'flex', justifyContent:'space-between', color:'red'}}>
                                    <span>차감 포인트:</span>
                                    <span>- {selectedProduct.price.toLocaleString()} P</span>
                                </div>
                                <hr style={{margin:'5px 0', borderTop:'1px dashed #ccc'}}/>
                                <div style={{display:'flex', justifyContent:'space-between', fontWeight:'bold'}}>
                                    <span>예상 잔액:</span>
                                    <span style={{color: userPoints - selectedProduct.price < 0 ? 'red' : 'blue'}}>
                                        {(userPoints - selectedProduct.price).toLocaleString()} P
                                    </span>
                                </div>
                                {/* 잔액 부족 경고 메시지 */}
                                {userPoints < selectedProduct.price && (
                                    <p style={{color:'red', marginTop:'5px', fontWeight:'bold', fontSize:'0.9em'}}>
                                        ※ 잔액이 부족합니다.
                                    </p>
                                )}
                            </div>
                        )}
                        
                        {/* 최종 결제 금액 */}
                        <div className="total-row" style={{textAlign:'right', fontSize:'1.2em'}}>
                            최종 결제: {selectedProduct.price.toLocaleString()}원
                        </div>

                        {/* 환불 규정 동의 체크박스 */}
                        <div style={{background:'#f9f9f9', padding:'10px', fontSize:'0.9em', border:'1px solid #ddd', marginTop:'10px'}}>
                            <label style={{cursor:'pointer', display:'flex', alignItems:'center'}}>
                                <input type="checkbox" checked={agreed} onChange={(e) => setAgreed(e.target.checked)} 
                                       style={{marginRight:'10px', transform:'scale(1.2)'}} />
                                <strong>[필수] 환불 규정 동의</strong>
                            </label>
                        </div>

                        {/* 하단 버튼 그룹 */}
                        <div style={{marginTop:'20px', display:'flex', gap:'10px'}}>
                            <button className="btn" style={{flex:1}} onClick={() => setIsModalOpen(false)}>취소</button>
                            <button className="btn-primary" style={{flex:2}} 
                                    onClick={handlePayment}
                                    // 포인트 부족 시 버튼 비활성화 (UX)
                                    disabled={paymentMethod === 'POINT' && userPoints < selectedProduct.price}>
                                결제하기
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      )}
    </div>
  );
}

export default TicketShopPage;