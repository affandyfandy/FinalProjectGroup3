export interface RouteLink {
    path: string;
    link: string;
  }
  
  export const RouterConfig = {
    CUSTOMER: { path: '', link: '/', title: 'Home' },
    AUTH: { path: 'auth', link: '/auth', title: 'Auth Page' },
    ADMIN: { path: 'admin', link: '/customer', title: 'Admin Page' },
    // ROOM: { path: 'rooms', link: '/room', title: 'Room Page' },
    // CREATE_ROOM: { path: 'rooms/create', link: '/rooms/create', title: 'Create Room' },
    // EDIT_ROOM: { path: 'rooms/edit/:id', link: '/rooms/edit/:id', title: 'Edit Room' },
    BOOKING: { path: 'bookings', link: '/booking', title: 'Booking Page' },
    NOT_FOUND: { path: '**', link: null, title: 'Page Not Found' }
  };