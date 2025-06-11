export enum RoleEnum {
    ADMIN = 'ADMIN',
    USER = 'USER'
}

export interface UserEntity {
    userId: string;
    email: string;
    password: string;
    userName: string;
    phoneNumber: string;
    address: string;
    date: Date;
    role: RoleEnum;
}

export default UserEntity;