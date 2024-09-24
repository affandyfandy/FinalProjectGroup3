export interface Room {
    id: string
    roomType: string
    roomNumber: string
    capacity: number
    status: string
    price: number
    hotelId: string
    photo: string
    facility: Facility[]; 
    createdBy?: string
    createdDate?: string
    lastModifiedBy?: string
    lastModifiedDate?: string
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

export enum RoomType {
  SINGLE = 'SINGLE',
  DOUBLE = 'DOUBLE',
  SUITE = 'SUITE'
}

export enum Status {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE'
}

export enum Facility {
  TELEVISION = 'Television',
  REFRIGERATOR = 'Refrigerator',
  MINIBAR = 'Minibar',
  WIFI = 'Wi-Fi',
  COFFEE_MAKER = 'Coffee Maker',
  HAIR_DRYER = 'Hair Dryer'
}
