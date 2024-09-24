export interface User {
    email: string;
    password?: string;
    fullName: string;
    role: string;
    phone: string;
    dateOfBirth: string;
    address: string;
    photo?: string;
    status: string;
    createdBy?: string;
    createdDate?: string;
    lastModifiedBy?: string;
    lastModifiedDate?: string;
}