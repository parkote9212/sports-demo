import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';

// 페이지 import
import LoginPage from './pages/LoginPage';
import MyTicketPage from './pages/MyTicketPage';
import TicketShopPage from './pages/TicketShopPage';
import PaymentSuccessPage from './pages/PaymentSuccessPage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* URL 매핑 설정 (Controller의 @RequestMapping과 유사) */}
        <Route path="/" element={<LoginPage />} />
        <Route path="/mypage/tickets" element={<MyTicketPage />} />
        <Route path="/mypage/shop" element={<TicketShopPage />} />
        <Route path="/payment/success" element={<PaymentSuccessPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;