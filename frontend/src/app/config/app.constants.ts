export const AppConstants = {
    APPLICATION_NAME: 'Hotel Client',
    BASE_API_V1_URL: 'http://localhost:8080/api/v1',
};

export const APIConstants = {
    USER: AppConstants.BASE_API_V1_URL + '/users',
    ROOM: AppConstants.BASE_API_V1_URL + '/rooms',
    RESERVATION: AppConstants.BASE_API_V1_URL + '/reservations',
    PAYMENT: AppConstants.BASE_API_V1_URL + '/payments',
};

export const Service = {
    USER: 'users',
    ROOM: 'rooms',
    BOOKING: 'bookings',
    PAYMENT: 'payments',
    RESERVATION: 'reservations',
};