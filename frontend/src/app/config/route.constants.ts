export interface RouteLink {
    path: string;
    link: string;
  }
  
  export const RouterConfig = {
    HOME: {path: '', link: '/'},
    LOGIN: {path: 'login', link: '/login', title: 'Login Page'},
    CUSTOMER: {path: 'customer', link: '/customer', title: 'Customer Page'},
    ROOM: {path: 'room', link: '/room', title: 'Room Page'},
    BOOKING: {path: 'booking', link: '/booking', title: 'Booking Page'},
    NOT_FOUND: {path: '**', link: null, title: 'Page Not Found'}
  };