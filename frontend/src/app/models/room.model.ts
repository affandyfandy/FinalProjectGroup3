export interface Room {
    id: string
    roomType: string
    capacity: number
    status: string
    price: number
    hotelId: string
    photo: string
    facility: string
    token: string
}

export interface RoomResponse{
  totalElements: number;
  totalPages: number;
  size: number;
  content: Room[];
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  first: boolean;
  last: boolean;
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  empty: boolean;
}