import dayjs from "dayjs";

export const formatDateTime = (selectedTime: string) => {
  if (!selectedTime) {
    return "";
  }

  const date = dayjs(selectedTime);
  const dayName = date.format("dddd");
  const dayMonth = date.format("DD/M");
  const time = date.format("HH:mm");

  return `${dayName.charAt(0).toUpperCase() + dayName.slice(1)}, ${dayMonth} às ${time}`;
};
