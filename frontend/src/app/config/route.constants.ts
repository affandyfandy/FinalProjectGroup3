export interface RouteLink {
    path: string;
    link: string;
  }
  
  export const RouterConfig = {
    HOME: {path: '', link: '/'},
    AUTH: {path: 'auth', link: '/auth', title: 'Auth Page'},
    USER: {path: 'users', link: '/user', title: 'User Page'},
    CUSTOMER: {path: 'customers', link: '/customer', title: 'Customer Page'},
    ROOM: {path: 'rooms', link: '/room', title: 'Room Page'},
    BOOKING: {path: 'bookings', link: '/booking', title: 'Booking Page'},
    NOT_FOUND: {path: '**', link: null, title: 'Page Not Found'}
  };