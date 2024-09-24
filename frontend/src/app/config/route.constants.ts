export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  CUSTOMER: { path: '', link: '/', title: 'Customer Page' },
  AUTH: { path: 'auth', link: '/auth', title: 'Auth Page' },
  ADMIN: { path: 'admin', link: '/admin', title: 'Admin Page' },
  ROOM: { path: 'rooms', link: '/rooms', title: 'Room Page' },
  RESERVATION: { path: 'reservation', link: '/reservation', title: 'Reservation Page' },
  USER: { path: 'users', link: '/users', title: 'User Page' },
  UNAUTHORIZED: { path: 'unauthorized', link: '/unauthorized', title: 'Unauthorized Page' },
  NOT_FOUND: { path: '**', link: null, title: 'Page Not Found' }
};
