export interface Payment {
  id: string;
  accountNumber: string;
  accountName: string;
  amount: number;
  reservationId: string;
  createdBy?: string;
  createdDate?: string;
  lastModifiedBy?: string;
  lastModifiedDate?: string;
}
